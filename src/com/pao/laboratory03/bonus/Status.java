package com.pao.laboratory03.bonus;

public enum Status {
    TODO {
        @Override
        public boolean canTransitionTo(Status next) {
            return next == IN_PROGRESS || next == CANCELLED;
        }
    },
    IN_PROGRESS {
        @Override
        public boolean canTransitionTo(Status next) {
            return next == DONE || next == CANCELLED;
        }
    },
    DONE {
        @Override
        public boolean canTransitionTo(Status next) {
            return false; // Din DONE nu mai poți merge nicăieri
        }
    },
    CANCELLED {
        @Override
        public boolean canTransitionTo(Status next) {
            return false; // Nici din CANCELLED
        }
    };

    // Fiecare stare de mai sus trebuie să dicteze regulile ei
    public abstract boolean canTransitionTo(Status next);
}