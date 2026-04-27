package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayDeque;
import java.util.Deque;

public class Order {
    private OrderState currentState;
    private final Deque<OrderState> history = new ArrayDeque<>();

    public Order(OrderState initialState) {
        this.currentState = initialState;
    }

    public void nextState() {
        if (currentState.isFinal()) {
            throw new OrderIsAlreadyFinalException("Order is already in a final state.");
        }
        history.push(currentState);
        currentState = currentState.next();
        System.out.println("Order state updated to: " + currentState);
    }

    public void cancel() {
        if (currentState.isFinal()) {
            throw new CannotCancelFinalOrderException("Cannot cancel a final state order.");
        }
        history.push(currentState);
        currentState = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException("Cannot revert initial order state.");
        }
        currentState = history.pop();
        System.out.println("Order state reverted to: " + currentState);
    }

    public OrderState getCurrentState() {
        return currentState;
    }
}
