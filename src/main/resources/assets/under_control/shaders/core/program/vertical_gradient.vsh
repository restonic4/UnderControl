#version 150

#moj_import <fog.glsl>

in vec3 Position;

uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

uniform vec3 Center;
uniform float Radius;

out float normalizedHeight;

void main() {
    normalizedHeight = (Position.y - (Center.y - Radius)) / (2.0 * Radius);
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}