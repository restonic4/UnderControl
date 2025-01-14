#version 150

#moj_import <fog.glsl>

// Uniforms
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec4 ColorModulator;

uniform vec4 TopColor;
uniform vec4 BottomColor;

in float vertexHeight;

out vec4 fragColor;

void main() {
    float heightFactor = (vertexHeight - BottomColor.y) / (TopColor.y - BottomColor.y);
    fragColor = mix(BottomColor, TopColor, heightFactor) * ColorModulator;
}