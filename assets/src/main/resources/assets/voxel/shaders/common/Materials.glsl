struct Material {
	vec4 diffuse;
	float roughness;
	float metallic;
	float specular;
	sampler2D diffuseTex;
	sampler2D normalTex;
	sampler2D roughnessTex;
	sampler2D metallicTex;
	sampler2D specularTex;
};