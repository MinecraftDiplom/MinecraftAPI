# MineProfile API

API разработано для MineProfile.
Телеграмм бот [клик](https://t.me/Koliy82Bot).
Мобильное приложение скачать можно командой /mobile в [боте](https://t.me/Koliy82Bot).

# Self Hosted

Для сборки проекта необходимо переименовать файл с примером конфигурации и установить свои переменные:
nmsr-skin-api/example.config.toml -> config.toml
src/main/resources/example.application.properties -> application.properties

После установки переменных необходимо открыть консоль, перейти в дирикторию с файлом docker-compose.yml и написать следующую команду:
docker compose up -d

## Supported render modes

<table>
    <thead>
        <tr>
            <th>Category</th>
            <th>Render Mode</th>
            <th>Description</th>
            <th>Example</th>
            <th>Example (Back)</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td rowspan="5">Body</td>
            <td>FullBody</td>
            <td>Full body render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-fullbody.png" width="100"></td>
            <td><img src=".nmsr-skin-api/assets/NickAc-fullbody-back.png" width="100"></td>
        </tr>
        <tr>
            <td>FullBodyIso</td>
            <td>Full body isometric render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-fullbodyiso.png" width="100"></td>
            <td><img src=".nmsr-skin-api/assets/NickAc-fullbodyiso-back.png" width="100"></td>
        </tr>
        <tr>
            <td>BodyBust</td>
            <td>Body bust render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-bodybust.png" width="100"></td>
            <td><img src=".nmsr-skin-api/assets/NickAc-bodybust-back.png" width="100"></td>
        </tr>
        <tr>
            <td>FrontBust</td>
            <td>Bust isometric front render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-frontbust.png" width="100"></td>
            <td></td>
        </tr>
        <tr>
            <td>FrontFull</td>
            <td>Full isometric front render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-frontfull.png" width="100"></td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="3">Head</td>
            <td>Head</td>
            <td>Head render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-head.png" width="100"></td>
            <td><img src=".nmsr-skin-api/assets/NickAc-head-back.png" width="100"></td>
        </tr>
        <tr>
            <td>HeadIso</td>
            <td>Head isometric render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-headiso.png" width="100"></td>
            <td><img src=".nmsr-skin-api/assets/NickAc-headiso-back.png" width="100"></td>
        </tr>
        <tr>
            <td>Face</td>
            <td>Face render</td>
            <td><img src=".nmsr-skin-api/assets/NickAc-face.png" width="100"></td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="2">Extra</td>
            <td>Skin</td>
            <td colspan="4">Player skin</td>
        </tr>
        <tr>
            <td>Custom</td>
            <td colspan="4">Custom render settings</td>
        </tr>
    </tbody>
</table>

## UV map layouts

Это текущая версия макета UV-карты. Это 8-битное RGBA-изображение.

<details>
    <!--Our Red channel is composed of the 6 bits of the u coordinate + 2 bits from the v coordinate
    U is used as-is because our coordinates are 0-63
    0   1   2   3   4   5   6   7
    [    ---- u ----    ]   [ v ]
    Our Green channel is composed of the 4 remaining bits of the v coordinate + 4 bits from the shading
    V is used as-is because our coordinates are 0-63
    0   1   2   3   4   5   6   7
    [  -- v --  ]   [  -- s --  ]
    Our Blue channel is composed of the 4 remaining bits of the shading + 4 bits from the depth
    0   1   2   3   4   5   6   7
    [  -- s --  ]   [  -- d --  ]
    Our Alpha channel is composed of the 8 remaining bits of the depth
    0   1   2   3   4   5   6   7
    [          -- d --          ]-->
    <summary>UV map layout v2</summary>
    <table>
    <tbody>
        <tr>
            <th align="center" colspan="8">R</td>
            <th align="center" colspan="8">G</td>
            <th align="center" colspan="8">B</td>
            <th align="center" colspan="8">A</td>
        </tr>
        <tr>
            <td align="center">0</td>
            <td align="center">1</td>
            <td align="center">2</td>
            <td align="center">3</td>
            <td align="center">4</td>
            <td align="center">5</td>
            <td align="center">6</td>
            <td align="center">7</td>
            <td align="center">8</td>
            <td align="center">9</td>
            <td align="center">10</td>
            <td align="center">11</td>
            <td align="center">12</td>
            <td align="center">13</td>
            <td align="center">14</td>
            <td align="center">15</td>
            <td align="center">16</td>
            <td align="center">17</td>
            <td align="center">18</td>
            <td align="center">19</td>
            <td align="center">20</td>
            <td align="center">21</td>
            <td align="center">22</td>
            <td align="center">23</td>
            <td align="center">24</td>
            <td align="center">25</td>
            <td align="center">26</td>
            <td align="center">27</td>
            <td align="center">28</td>
            <td align="center">29</td>
            <td align="center">30</td>
            <td align="center">31</td>
        </tr>
        <tr>
            <th align="center" colspan="6">U</td>
            <th align="center" colspan="6">V</td>
            <th align="center" colspan="8">Shading</td>
            <th align="center" colspan="12">Depth</td>
        </tr>
    </tbody>
    </table>
</details>