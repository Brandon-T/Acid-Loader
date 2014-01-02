package acid.loaders;

import acid.OperationMode;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

class AcidGLDXLoader {

    private int ByteSize = 0;
    private IntBuffer IBuffer = null;
    private ByteBuffer BBuffer = null;
    private OperationMode Mode = null;
    private BufferedImage Image = null;
    public static boolean OpenGLLoaded = false;
    public static boolean DirectXLoaded = false;
    //ByteSize = ((ImageWidth * BitsPerPixel + 31) / 32) * 4 * ImageHeight;

    private static native void GetOpenGLBuffer(IntBuffer Buffer);
    private static native void GetDirectXBuffer(IntBuffer Buffer);
    
    public AcidGLDXLoader(ByteBuffer Buffer, int ImageWidth, int ImageHeight) {
        this.BBuffer = Buffer;
        this.IBuffer = BBuffer.asIntBuffer();
    }

    public AcidGLDXLoader(int ImageWidth, int ImageHeight) {
        ByteSize = ImageWidth * ImageHeight * 4;
        BBuffer = ByteBuffer.allocateDirect(ByteSize).order(ByteOrder.LITTLE_ENDIAN);
        IBuffer = BBuffer.asIntBuffer();
    }

    public void SetSize(int ImageWidth, int ImageHeight) {
        BBuffer = null;
        IBuffer = null;
        ByteSize = ImageWidth * ImageHeight * 4;
        BBuffer = ByteBuffer.allocateDirect(ByteSize).order(ByteOrder.LITTLE_ENDIAN);
        IBuffer = BBuffer.asIntBuffer();
    }
    
    private void readBuffer(IntBuffer Buffer, OperationMode Mode) {
        Buffer.rewind();
        switch (Mode) {
            case OPENGL: GetOpenGLBuffer(Buffer);
            case DIRECTX: GetDirectXBuffer(Buffer);
            default: break;
        }
    }
    
    public void setMode(OperationMode Mode) {
        switch (this.Mode = Mode) {
            case OPENGL: OpenGLLoaded = true; break;
            case DIRECTX: DirectXLoaded = true; break;
            default: break;
        }
    }

    public ByteBuffer GetBuffer() {
        readBuffer(IBuffer, Mode);
        return BBuffer;
    }

    public void GetBuffer(ByteBuffer Buffer) {
        readBuffer(Buffer.asIntBuffer(), Mode);
    }

    public void GetBuffer(IntBuffer Buffer) {
        readBuffer(Buffer, Mode);
    }

    public BufferedImage VerticalFlip(BufferedImage imageIn) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -Image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        imageIn = op.filter(imageIn, null);
        return imageIn;
    }

    public BufferedImage HorizontalFlip(BufferedImage imageIn) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-Image.getHeight(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        imageIn = op.filter(imageIn, null);
        return imageIn;
    }

    public BufferedImage AxisFlip(BufferedImage imageIn) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-Image.getWidth(null), -Image.getWidth(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        imageIn = op.filter(imageIn, null);
        return imageIn;
    }
}
