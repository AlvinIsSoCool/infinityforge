#version 150

uniform sampler2D Sampler0;
uniform vec4 GlintColor;
uniform float GlintTime;
uniform vec2 ScreenSize;

out vec4 fragColor;

void main() {
    vec2 uv = (gl_FragCoord.xy / ScreenSize) + vec2(GlintTime * 0.15, GlintTime * 0.15);
    vec4 tex = texture(Sampler0, uv);
    float brightness = dot(tex.rgb, vec3(0.299, 0.587, 0.114));
    fragColor = vec4(GlintColor.rgb * brightness * 1.5, GlintColor.a);
    if (fragColor.a < 0.01) discard;
}