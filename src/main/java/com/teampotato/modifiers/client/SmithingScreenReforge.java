package com.teampotato.modifiers.client;

@SuppressWarnings("unused")
public interface SmithingScreenReforge {
    void modifiers_init();

    boolean modifiers_isOnTab2();

    void modifiers_setCanReforge(boolean canReforge);
}