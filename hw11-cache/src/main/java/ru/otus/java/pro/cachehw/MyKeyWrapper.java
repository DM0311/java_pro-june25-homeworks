package ru.otus.java.pro.cachehw;

public final class MyKeyWrapper {
    private String key;

    public MyKeyWrapper(Long key) {
        this.key = String.valueOf(key);
    }

    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
