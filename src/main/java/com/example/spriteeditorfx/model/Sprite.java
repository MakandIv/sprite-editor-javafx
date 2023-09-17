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
        setImage(image);

        int spriteInRow = (int) image.getWidth() / spriteWidth;

        int numberRow = (position - 1) / spriteInRow;
        int numberColumn = (position - 1) % spriteInRow;

        setViewport(new Rectangle2D(numberColumn * spriteWidth, numberRow * spriteHeight, spriteWidth, spriteHeight));
    }

}
