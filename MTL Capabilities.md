# Inputs

| Name        | Description                          | Wikipedia | In javagl/obj | In FOML | Used by FOML |
| ----------- | ------------------------------------ | --------- | ------------- | ------- | ------------ |
| newmtl      | Name                                 | [x]       | [x]           | [x]     | [x]          |
| Ka          | Ambient color                        | [x]       | [x]           | [x]     | NO           |
| Kd          | Diffuse color                        | [x]       | [x]           | [x]     | [x]          |
| Ks          | Specular color                       | [x]       | [x]           | [x]     | NO           |
| illum       | Luminosity                           | [x]       | NO            | [x]     | [x]          |
| d           | Disolve / Transparency / Opactity    | [x]       | [x]           | [x]     | NO           |
| map_Ka      | Ambient texture map                  | [x]       | NO            | NO      | NO           |
| map_Kd      | Diffuse texture map / texture        | [x]       | [x]           | [x]     | [x]          |
| map_Ks      | Specular color texture map           | [x]       | NO            | NO      | NO           |
| map_Ns      | Specular highlight component         | [x]       | NO            | NO      | NO           |
| map_d       | Transparency texture map             | [x]       | [x]           | [x]     | NO           |
| map_bump    | Bump map                             | [x]       | NO            | NO      | NO           |
| bump        | Bump map                             | [x]       | NO            | NO      | NO           |
| disp        | Displacement map                     | [x]       | NO            | NO      | NO           |
| decal       | Decal map                            | [x]       | NO            | NO      | NO           |
| Ns          | Specular exponent                    | [x]       | [x]           | [x]     | NO           |
| Ni          | Optique density                      | [x]       | NO            | NO      | NO           |
| tintindex   | Color Index                          | NO        | NO            | [x]     | [x]          |
| use_diffuse | Enable Kd (if FOML) for optimisation | NO        | NO            | [x]     | [x]          |
