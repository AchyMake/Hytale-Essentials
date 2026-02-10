package org.achymake.essentials.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class Account implements Component<EntityStore> {
    private double balance;
    public static final BuilderCodec<Account> CODEC =
            BuilderCodec.builder(Account.class, Account::new)
                    .append(new KeyedCodec<>("Balance", Codec.DOUBLE),
                            (account, Balance) -> account.balance = Balance,
                            account -> account.balance)
                    .add()
                    .build();
    public Account() {
        this.balance = 0.0;
    }
    public Account(Account clone) {
        this.balance = clone.balance;
    }
    public double getBalance() {
        return this.balance;
    }
    public boolean has(double amount) {
        return balance >= amount;
    }
    public void add(double amount) {
        this.balance += amount;
    }
    public void remove(double amount) {
        this.balance -= amount;
    }
    public void set(double value) {
        this.balance = value;
    }
    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new Account(this);
    }
}