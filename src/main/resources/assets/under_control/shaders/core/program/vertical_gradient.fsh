#version 150

#moj_import <fog.glsl>

// Uniforms
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec4 ColorModulator;

uniform vec4 TopColor;
uniform vec4 BottomColor;

in float normalizedHeight;

out vec4 fragColor;

void main() {
    float heightFactor = clamp(normalizedHeight, 0.0, 1.0);
    fragColor = mix(BottomColor, TopColor, heightFactor) * ColorModulator;
}