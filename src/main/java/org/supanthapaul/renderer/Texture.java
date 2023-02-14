package org.supanthapaul.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int texID, width, height;

//    public Texture(String filepath) {
//
//    }

    public void init(String filepath) {
        this.filepath = filepath;

        // generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // set texture parameters
        // repeat texture on both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When shrinking the texture, pixelate (for pixel art games)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // when stretching the texture, also pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // stbi stores width, height, and number of channels in IntBuffer
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        // flip the image vertically on load because opengl flips it
        stbi_set_flip_vertically_on_load(true);
        // load image with stbi
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if(image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

            // upload image to GPU
            if(channels.get(0) == 3) {
                // it's rgb
                glTexImage2D(GL_TEXTURE_2D,0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            else if (channels.get(0) == 4) {
                // it's rgba
                glTexImage2D(GL_TEXTURE_2D,0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else {
                // image is in a bad format
                assert false : "Error: (Texture) image has unknown number of channels: " + filepath;
            }
        } else {
            assert false : "Error: (Texture) could not load image: " + filepath;
        }

        // free the memory that was used to load the image
        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
