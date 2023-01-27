package org.supanthapaul.components;

import org.supanthapaul.contour.Component;

public class SpriteRenderer extends Component {

    private boolean firstTime = false;
    @Override
    public void start() {
        System.out.println("SpriteRenderer is starting");
    }
    @Override
    public void update(float dt) {
        if(!firstTime) {
            System.out.println("SpriteRenderer is updating");
            firstTime = true;
        }
    }
}
