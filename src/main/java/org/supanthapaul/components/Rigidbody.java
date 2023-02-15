package org.supanthapaul.components;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.supanthapaul.contour.Component;

public class Rigidbody extends Component {
    private int colliderType = 0;
    private float friction = 0.3f;
    private Vector3f velocity = new Vector3f(0, 0.5f, 0);
    public transient Vector4f tmp = new Vector4f(0, 0, 0, 0);
}