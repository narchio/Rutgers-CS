package Chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.android56.R;

import Pieces.Piece;

public class Cell {

    private Color color;
    private Piece piece;
    private Rect rect;
    int x;
    int y;

    public Cell(Color color, int x, int y, int left, int top, int right, int bottom) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.rect = new Rect(left, top, right, bottom);
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public Color getColor() {
        return this.color;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public Rect getRect() { return this.rect; }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setRect(int left, int top, int right, int bottom) { this.rect.set(left, top, right, bottom); }

    public void copyCell(Cell original) {
        this.color = original.getColor();
        this.piece = original.getPiece();
        this.rect = original.getRect();
    }

    public void drawCell(Canvas canvas, Context context) {
        Paint paint;
        paint = new Paint();
        paint.setStrokeWidth(4);
        if(this.color == Color.DARK_BROWN) {
            paint.setColor(android.graphics.Color.parseColor("#826f41"));
        }
        else{
            paint.setColor(android.graphics.Color.parseColor("#ab945e"));
        }
        canvas.drawRect(this.rect, paint);

        if(this.piece != null) {
            Bitmap bitmap= null;
            switch(this.piece.getPieceType()) {
                case ROOK:
                    if(this.piece.getColor() == Color.BLACK) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.black_rook);
                    }
                    else{
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_rook);
                    }
                    break;
                case KNIGHT:
                    if(this.piece.getColor() == Color.BLACK) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.black_knight);
                    }
                    else{
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_knight);
                    }
                    break;
                case BISHOP:
                    if(this.piece.getColor() == Color.BLACK) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.black_bishop);
                    }
                    else{
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_bishop);
                    }
                    break;
                case QUEEN:
                    if(this.piece.getColor() == Color.BLACK) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.black_queen);
                    }
                    else{
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_queen);
                    }
                    break;
                case KING:
                    if(this.piece.getColor() == Color.BLACK) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.black_king);
                    }
                    else{
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_king);
                    }
                    break;
                case PAWN:
                    if(this.piece.getColor() == Color.BLACK) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.black_pawn);
                    }
                    else{
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_pawn);
                    }
                    break;
            }
            canvas.drawBitmap(bitmap, this.rect.left, this.rect.top, null);
        }
    }
}

