#version 150

#moj_import <fog.glsl>

in vec3 Position;

uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

out float vertexHeight;

void main() {
    vec4 worldPosition = ModelViewMat * vec4(Position, 1.0);

    vertexHeight = worldPosition.y;

    gl_Position = ProjMat * worldPosition;
}