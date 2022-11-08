package fr.flavinus.app.payload;

public class PdfCheckerPayload {

    private long offset;

    public PdfCheckerPayload(long offset) {
        this.offset = offset;
    }

    public long getOffset() {
        return offset;
    }
}