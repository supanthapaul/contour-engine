package org.supanthapaul.components;

import org.joml.Vector2f;
import org.supanthapaul.renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private Texture texture;
    private List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        // starting from the top left sprite
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for(int i =0; i < numSprites; i++) {
            // calculate bounds in normalized device coordinates
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            // Create texture coords and sprite based on the normalized bounds
            Vector2f[] texCoords= {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY),
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            // update current x,y coords
            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()) {
                // move to next row
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    public int size() {
        return sprites.size();
    }
}
