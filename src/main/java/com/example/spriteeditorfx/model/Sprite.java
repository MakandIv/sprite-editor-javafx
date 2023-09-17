package com.example.spriteeditorfx.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView {

    public Sprite(Image image, int position) {
        getSprite(image, position, 32, 32);
    }

    public Sprite(Image image, int position, int spriteWidth, int spriteHeight) {
        getSprite(image, position, spriteWidth, spriteHeight);
    }

    private void getSprite(Image image, int position, int spriteWidth, int spriteHeight) {
        int spriteInRow = (int) Math.round(Math.ceil(image.getWidth() / spriteWidth));
        int spriteInColumn = (int) Math.round(Math.ceil(image.getHeight() / spriteHeight));
        int numberCells = spriteInRow * spriteInColumn;

        Rectangle2D[] cellClips = new Rectangle2D[numberCells];
        for (int i = 0; i < spriteInColumn; i++) {
            for (int j = 0; j < spriteInRow; j++) {
                cellClips[i * spriteInRow + j] = new Rectangle2D(
                        j * spriteWidth, i * spriteHeight,
                        spriteWidth, spriteHeight
                );
            }
        }

        setImage(image);
        setViewport(cellClips[position - 1]);
    }

}
