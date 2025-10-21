package com.pi.pi_cloud.lib;

public class AesResult {
    private byte[] iv;
    private byte[] textoCifrado;

    public AesResult(byte[] iv, byte[] textoCifrado) {
        this.iv = iv;
        this.textoCifrado = textoCifrado;
    }

    public byte[] toBlob() {
        byte[] blob = new byte[iv.length + textoCifrado.length];
        System.arraycopy(iv, 0, blob, 0, iv.length);
        System.arraycopy(textoCifrado, 0, blob, iv.length, textoCifrado.length);
        return blob;
    }
}
