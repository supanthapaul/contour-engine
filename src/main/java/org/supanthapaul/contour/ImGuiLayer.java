package org.supanthapaul.contour;

import imgui.ImGui;

public class ImGuiLayer {
    public void imgui() {
        ImGui.begin("Cool window");
        if(ImGui.button("Hello button")) {
            System.out.println("Hello world");
        }
        ImGui.end();
    }
}
