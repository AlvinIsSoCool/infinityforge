/*#version 150

uniform sampler2D Sampler0;
uniform vec4 GlintColor;
uniform float GlintTime;
uniform vec2 ScreenSize;

out vec4 fragColor;

void main() {
    vec2 uv = (gl_FragCoord.xy / ScreenSize) + vec2(GlintTime * 0.1, GlintTime * 0.1);
    vec4 tex = texture(Sampler0, uv);
    float brightness = dot(tex.rgb, vec3(0.299, 0.587, 0.114));
    fragColor = vec4(GlintColor.rgb * brightness * 1.5, GlintColor.a);
    if (fragColor.a < 0.01) discard;
}*/

#version 150

uniform sampler2D Sampler0;
uniform vec4 GlintColor;
uniform float GlintTime;
uniform vec2 ScreenSize;

out vec4 fragColor;

vec2 diagonalUV(vec2 uv, float angle, float speed, float scale) {
    float c = cos(angle), s = sin(angle);
    mat2 rot = mat2(c, -s, s, c);
    vec2 p = rot * uv * scale;
    p.x += GlintTime * speed;
    return p;
}

// 5-tap box blur
vec4 blurSample(vec2 uv, float spread) {
    vec4 sum = texture(Sampler0, uv) * 0.4;
    sum += texture(Sampler0, uv + vec2(spread, 0.0)) * 0.15;
    sum += texture(Sampler0, uv - vec2(spread, 0.0)) * 0.15;
    sum += texture(Sampler0, uv + vec2(0.0, spread)) * 0.15;
    sum += texture(Sampler0, uv - vec2(0.0, spread)) * 0.15;
    return sum;
}

void main() {
    vec2 uv = gl_FragCoord.xy / ScreenSize;
    uv.x *= ScreenSize.x / ScreenSize.y;

    vec2 uv1 = diagonalUV(uv,  0.610865, 0.41, 1.62); // ~35°
    vec2 uv2 = diagonalUV(uv, -0.261799, 0.29, 1.44); // ~-15°
    vec4 tex1 = blurSample(uv1, 0.02);
    vec4 tex2 = blurSample(uv2, 0.02);

    // ITU-R BT.601 luminance formula is used for getting maximum brightness from the texture.
    float brightness = pow(dot(tex1.rgb * 0.6 + tex2.rgb * 0.4, vec3(0.299, 0.587, 0.114)), 1.1);
    fragColor = vec4(GlintColor.rgb * brightness * 1.5, GlintColor.a);
}