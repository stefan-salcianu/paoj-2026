package com.pao.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class IncorrectExtensionException extends Exception {
    public IncorrectExtensionException(String expectedExtension, String filename) {
        super("Filename must end with '" + expectedExtension + "' but got: " + filename);
    }
}

public class DiffGenerator {
    public static void saveDiff(String expected, String actual, String filename) throws IncorrectExtensionException {
        if (!filename.endsWith(".diff")) {
            throw new IncorrectExtensionException(".diff", filename);
        }

        String[] expLines = expected.replace("\r\n", "\n").split("\n", -1);
        String[] actLines = actual.replace("\r\n", "\n").split("\n", -1);
        int n = expLines.length, m = actLines.length;

        // LCS table
        int[][] lcs = new int[n + 1][m + 1];
        for (int i = n - 1; i >= 0; i--)
            for (int j = m - 1; j >= 0; j--)
                lcs[i][j] = expLines[i].equals(actLines[j])
                        ? lcs[i + 1][j + 1] + 1
                        : Math.max(lcs[i + 1][j], lcs[i][j + 1]);

        // Build edit ops: 0=context, 1=removed(-), 2=added(+)
        record Op(int type, String line) {}
        List<Op> ops = new ArrayList<>();
        int i = 0, j = 0;
        while (i < n && j < m) {
            if (expLines[i].equals(actLines[j])) {
                ops.add(new Op(0, expLines[i++])); j++;
            } else if (lcs[i + 1][j] >= lcs[i][j + 1]) {
                ops.add(new Op(1, expLines[i++]));
            } else {
                ops.add(new Op(2, actLines[j++]));
            }
        }
        while (i < n) ops.add(new Op(1, expLines[i++]));
        while (j < m) ops.add(new Op(2, actLines[j++]));

        // Generate unified diff hunks (context=3)
        final int CTX = 3;
        List<String> diff = new ArrayList<>();
        diff.add("--- expected_output.txt");
        diff.add("+++ actual_output.txt");

        int k = 0;
        while (k < ops.size()) {
            while (k < ops.size() && ops.get(k).type() == 0) k++;
            if (k >= ops.size()) break;

            int hStart = Math.max(0, k - CTX);
            int hEnd = k;
            while (hEnd < ops.size() && ops.get(hEnd).type() != 0) hEnd++;
            hEnd = Math.min(ops.size(), hEnd + CTX);

            // Compute line numbers
            int expStart = 1, actStart = 1, expCount = 0, actCount = 0;
            for (int x = 0; x < hStart; x++) {
                if (ops.get(x).type() != 2) expStart++;
                if (ops.get(x).type() != 1) actStart++;
            }
            for (int x = hStart; x < hEnd; x++) {
                if (ops.get(x).type() != 2) expCount++;
                if (ops.get(x).type() != 1) actCount++;
            }

            diff.add(String.format("@@ -%d,%d +%d,%d @@", expStart, expCount, actStart, actCount));
            for (int x = hStart; x < hEnd; x++) {
                Op op = ops.get(x);
                String prefix = op.type() == 0 ? " " : (op.type() == 1 ? "-" : "+");
                diff.add(prefix + op.line());
            }
            k = hEnd;
        }

        try {
            Path outputPath = Path.of(filename);
            Files.write(outputPath, diff);
            System.out.println("Diff file generated successfully at: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to write diff file: " + e.getMessage());
        }
    }
}
