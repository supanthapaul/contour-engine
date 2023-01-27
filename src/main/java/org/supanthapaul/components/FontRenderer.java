package org.supanthapaul.components;

import org.supanthapaul.contour.Component;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if(gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Sprint renderer!!");
        }
    }

    @Override
    public void update(float dt) {

    }
}
