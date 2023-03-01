package org.supanthapaul.components;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.supanthapaul.contour.Window;
import org.supanthapaul.renderer.DebugDraw;
import org.supanthapaul.util.Settings;

public class GridLines extends Component {

    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getCurrentScene().getCamera().position;
        Vector2f projectionSize = Window.getCurrentScene().getCamera().getProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        int numVtLines = (int)(projectionSize.x / Settings.GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y / Settings.GRID_HEIGHT) + 2;

        int width = (int) projectionSize.x + Settings.GRID_WIDTH * 2;
        int height = (int) projectionSize.y + Settings.GRID_HEIGHT * 2;

        int maxLines = Math.max(numHzLines, numVtLines);
        Vector3f lineColor = new Vector3f(0.2f, 0.2f, 0.2f);
        for(int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if( i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), lineColor);
            }
            if(i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), lineColor);
            }
        }
    }
}
