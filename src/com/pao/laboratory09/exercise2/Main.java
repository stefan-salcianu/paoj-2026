package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        new File("output").mkdirs();

        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                String[] parts = scanner.nextLine().trim().split(" ");
                int id = Integer.parseInt(parts[0]);
                double suma = Double.parseDouble(parts[1]);
                String data = parts[2];
                TipTranzactie tip = TipTranzactie.valueOf(parts[3]);

                byte[] record = new byte[RECORD_SIZE];
                ByteBuffer buf = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
                buf.putInt(id);
                buf.putDouble(suma);

                byte[] dataBytes = String.format("%-10s", data).getBytes(StandardCharsets.US_ASCII);
                buf.put(dataBytes, 0, 10);
                buf.put((byte) (tip == TipTranzactie.CREDIT ? 0 : 1));
                buf.put((byte) 0); // status = PENDING

                dos.write(record);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("READ ")) {
                    int idx = Integer.parseInt(line.substring(5).trim());
                    System.out.println(readRecord(raf, idx));

                } else if (line.startsWith("UPDATE ")) {
                    String[] parts = line.split(" ");
                    int idx = Integer.parseInt(parts[1]);
                    String statusStr = parts[2];
                    byte statusByte = statusToByte(statusStr);
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusByte);
                    System.out.println("Updated [" + idx + "]: " + statusStr);

                } else if (line.equals("PRINT_ALL")) {
                    long count = raf.length() / RECORD_SIZE;
                    for (int i = 0; i < count; i++) {
                        System.out.println(readRecord(raf, i));
                    }
                }
            }
        }
    }

    private static String readRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer buf = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
        int id = buf.getInt();
        double suma = buf.getDouble();

        byte[] dataBytes = new byte[10];
        buf.get(dataBytes);
        String data = new String(dataBytes, StandardCharsets.US_ASCII).trim();

        int tipByte = buf.get() & 0xFF;
        int statusByte = buf.get() & 0xFF;

        String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
        String status = byteToStatus(statusByte);

        return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status);
    }

    private static byte statusToByte(String status) {
        return switch (status) {
            case "PROCESSED" -> 1;
            case "REJECTED" -> 2;
            default -> 0;
        };
    }

    private static String byteToStatus(int b) {
        return switch (b) {
            case 1 -> "PROCESSED";
            case 2 -> "REJECTED";
            default -> "PENDING";
        };
    }
}
