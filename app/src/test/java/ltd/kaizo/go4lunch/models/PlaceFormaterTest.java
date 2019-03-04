package ltd.kaizo.go4lunch.models;

import android.location.Location;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import ltd.kaizo.go4lunch.models.API.placeDetail.PlaceDetailApiData;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * The type Place formater test.
 */
public class PlaceFormaterTest {
    /**
     * The Place formater.
     */
    private PlaceFormater placeFormater;
    /**
     * The Mocked location.
     */
    private Location mockedLocation = mock(Location.class);

    /**
     * Sets .
     */
    @Before
    public void setup() {
        String apiData = "{\n" +
                "   \"html_attributions\" : [],\n" +
                "   \"result\" : {\n" +
                "      \"formatted_address\" : \"223 Route de Trégastel, 22300 Lannion, France\",\n" +
                "      \"formatted_phone_number\" : \"02 96 48 26 79\",\n" +
                "      \"geometry\" : {\n" +
                "         \"location\" : {\n" +
                "            \"lat\" : 48.76555889999999,\n" +
                "            \"lng\" : -3.478602099999999\n" +
                "         },\n" +
                "         \"viewport\" : {\n" +
                "            \"northeast\" : {\n" +
                "               \"lat\" : 48.76689803029149,\n" +
                "               \"lng\" : -3.477264019708498\n" +
                "            },\n" +
                "            \"southwest\" : {\n" +
                "               \"lat\" : 48.76420006970849,\n" +
                "               \"lng\" : -3.479961980291502\n" +
                "            }\n" +
                "         }\n" +
                "      },\n" +
                "      \"name\" : \"La Tête de Goinfre\",\n" +
                "      \"opening_hours\" : {\n" +
                "         \"open_now\" : false,\n" +
                "         \"periods\" : [\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 1,\n" +
                "                  \"time\" : \"1400\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 1,\n" +
                "                  \"time\" : \"1200\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 2,\n" +
                "                  \"time\" : \"1400\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 2,\n" +
                "                  \"time\" : \"1200\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 2,\n" +
                "                  \"time\" : \"2100\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 2,\n" +
                "                  \"time\" : \"1900\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 3,\n" +
                "                  \"time\" : \"1400\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 3,\n" +
                "                  \"time\" : \"1200\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 3,\n" +
                "                  \"time\" : \"2100\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 3,\n" +
                "                  \"time\" : \"1900\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 4,\n" +
                "                  \"time\" : \"1400\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 4,\n" +
                "                  \"time\" : \"1200\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 5,\n" +
                "                  \"time\" : \"1400\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 5,\n" +
                "                  \"time\" : \"1200\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 5,\n" +
                "                  \"time\" : \"2100\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 5,\n" +
                "                  \"time\" : \"1900\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 6,\n" +
                "                  \"time\" : \"1400\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 6,\n" +
                "                  \"time\" : \"1200\"\n" +
                "               }\n" +
                "            },\n" +
                "            {\n" +
                "               \"close\" : {\n" +
                "                  \"day\" : 6,\n" +
                "                  \"time\" : \"2100\"\n" +
                "               },\n" +
                "               \"open\" : {\n" +
                "                  \"day\" : 6,\n" +
                "                  \"time\" : \"1900\"\n" +
                "               }\n" +
                "            }\n" +
                "         ],\n" +
                "         \"weekday_text\" : [\n" +
                "            \"lundi: 12:00 – 14:00\",\n" +
                "            \"mardi: 12:00 – 14:00, 19:00 – 21:00\",\n" +
                "            \"mercredi: 12:00 – 14:00, 19:00 – 21:00\",\n" +
                "            \"jeudi: 12:00 – 14:00\",\n" +
                "            \"vendredi: 12:00 – 14:00, 19:00 – 21:00\",\n" +
                "            \"samedi: 12:00 – 14:00, 19:00 – 21:00\",\n" +
                "            \"dimanche: Fermé\"\n" +
                "         ]\n" +
                "      },\n" +
                "      \"photos\" : [\n" +
                "         {\n" +
                "            \"height\" : 1080,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/110940932544238898892/photos\\\"\\u003eQuentin LE MEUR\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAA2Md1Hp2RGMWYc80CJgL8_n4Zjbf-ZhZgWMNR2nr7gNdUroKXlZuhJG77qzWtoyEY2r0FKv_2CjRgtLUnlqu_uTj0PnRtrvpf-LgSii1D3aFlP8OCBmqrCw4ukPbnczjGEhBIZJd-cTSCnwP63kVHVfe-GhQRfuzOODqbd8vsWduzTzpTtkFGAw\",\n" +
                "            \"width\" : 1920\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 720,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/105817925087013648046/photos\\\"\\u003eDjO X\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAA2OhZRlj1cXcKUBINlh6GZXum5w5GH7xOk7J0edX4deztaD40i47_vPYOqjdr5w0OqG0ww0aLvR6PXzivA8frRa8WxXzGm-_WpyPFBi-NXBsalwZjjVRIr8zzEIW7WmdSEhB2iL6rkrReX_YoWeX9-YcoGhTAMGLCH7gXqGXLxMN7mXRAAT41OQ\",\n" +
                "            \"width\" : 1030\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 2160,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/107621712990039120798/photos\\\"\\u003eSerge RAYMOND\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAAVPfm2tRYhZL2gMOC9tRjYJPTE9AvtJDMZRfCig1vC5Uc82ZaTkFPgE119nobO2-HZ1hpKl9dMQ9yodASRgzRQQHJnJm0uPNWgaNLo4cBEWvzwpj8N2EHkVJaMNhSfxzlEhAuYmbZy75CrA_JsPkYTY5XGhQqxQttNzoV47yIDZa35ZUImau4Gw\",\n" +
                "            \"width\" : 3840\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 4032,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/117252466574231538408/photos\\\"\\u003eAlain Cardon\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAARQ0PVFXbkGwBuR6gXKNq3R_u_rY7MgAFK4OE9gIcL1NzMiIXeMK7WZdagk5xlrS8bk3GaJs8omKcg0hXOV8EcuemkvIDkasekVMYT1DzMqD5e420tvad4Ld3F2CRhIN0EhDONkPRfiT9dIf5xthpDA9vGhTS-QbOZjR0FRnAznOdl5J0_bM53w\",\n" +
                "            \"width\" : 3024\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 1365,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/104115812588445483660/photos\\\"\\u003eLa Tête de Goinfre\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAAnscn0eCUU8bvMR46RQ07UcQtZITcNZcAmFrjiurO64Ryf4K49Et80pl3gBGsf08h9az-DuGI_7e4GuqIaZz5KaZOx0cKNdJ62ixlNyv4A_ZguydCdz8jZDmZFbUdwA3REhDl_JAGQtcpqpV7Pim9GGgBGhRHY-OYeT_fOmtTgZgjGZjHpqnNQg\",\n" +
                "            \"width\" : 2048\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 2976,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/102456943030448352837/photos\\\"\\u003eChristian Kokkinis\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAAF66B-Nq1FcNH8YlzvJJbdh86-tZZRYKseKuquO4XfSuiJP1S_-ekFNZ_OHMtSk384UaagSo8AkSZEW5ylTEF40QZd6JsegvpxsbflADdnhJLXB8PK_7SJPPjokAQwewDEhCXp94H-K-r9BuXequxHPS8GhTedVUNoB1sl43UtoQAZA708qK5kg\",\n" +
                "            \"width\" : 3968\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 3840,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/110688068194685139680/photos\\\"\\u003eFabrice Dufour\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAAyxd2NCOoE0WmLyPDmray6ohafFk6ddVqR_ZMnU_vEZ7waOZL2sjaAlwwkGFMlr9eytICt0JUqHeuq-9BKHbIS9AlvYfrVEnhV7nFGSMFolX_Ns_8uiaJZeEiRxFEwC_SEhB3vZ5BQ5Iv0j_Xt0bGkv1vGhRYe1XEQGq_kXiVvvKQKoZ4O1ljcg\",\n" +
                "            \"width\" : 2160\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 3968,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/106061690506974672836/photos\\\"\\u003eCatherine Chenevard\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAAv4baDFeaZLuY_csQHc2BkAc6Z1zmzb5Jc1KoMRpQU_CR_Epm6HpBZgJvXowHyktd-xHesvlFAluUcKZIPvsShj79hFHOo599B2TmFiVUK8q4R47r-VOJI7o0pGapgg7aEhBZAGyi3h6xUAn3Zl8EdpFcGhTSo9f8jR4SapfxArpT_Z39ZCzcjQ\",\n" +
                "            \"width\" : 2976\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 3968,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/102456943030448352837/photos\\\"\\u003eChristian Kokkinis\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAAb4XIuJum-mPXRR1cCGDlFOrm1l3E2xrTKhYFp8Lz8JkEGsEdvV7CIBPIDZeqR5GAK8zPiQxhmePIzCbiChWxp4b7d1aRDWv-y_O0xaJzP9vNU3HQYCc8R5Tm-GNhMqXkEhDkXzjPpDcWgXOUB_1G7qzsGhQwifbz3WycIKnPQ8zv8Pu_9yjJ9A\",\n" +
                "            \"width\" : 2976\n" +
                "         },\n" +
                "         {\n" +
                "            \"height\" : 2560,\n" +
                "            \"html_attributions\" : [\n" +
                "               \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/112870396552976704908/photos\\\"\\u003eCathy Bouchez\\u003c/a\\u003e\"\n" +
                "            ],\n" +
                "            \"photo_reference\" : \"CmRaAAAAwj89Sk0fqDYms0NFnLAyHCV3QPaSJKVyas_togAtEDCJ5h530eLilWLJyEuUmiUaGFiozSyREu5mDY7un5pQZ75VxqvBta1iAcABYGU-R_i6uV9VQEn6-yhcut9HqKYNEhCuUpOS_21gEE9onOb_u5a2GhSKxYKhZI2wSqiD34YCOKAUmdrCxw\",\n" +
                "            \"width\" : 1536\n" +
                "         }\n" +
                "      ],\n" +
                "      \"rating\" : 4.3,\n" +
                "      \"vicinity\" : \"223 Route de Trégastel, Lannion\",\n" +
                "      \"website\" : \"http://www.tetedegoinfre.com/\"\n" +
                "   },\n" +
                "   \"status\" : \"OK\"\n" +
                "}";


        mockedLocation = new Location("Gps");
        mockedLocation.setLongitude(2.294694);
        mockedLocation.setLatitude(48.858093);
        Gson gson = new Gson();
        PlaceDetailApiData placeDetailResult = gson.fromJson(apiData, PlaceDetailApiData.class);
        this.placeFormater = new PlaceFormater(placeDetailResult.getResult(), mockedLocation);
    }

    /**
     * Give api data to place formater should return place name data.
     */
    @Test
    public void GiveApiDataToPlaceFormaterShouldReturnPlaceNameData() {

        assertEquals("La Tête de Goinfre", placeFormater.getPlaceName());

    }

    /**
     * Give api data to place formater should return place phone.
     */
    @Test
    public void GiveApiDataToPlaceFormaterShouldReturnPlacePhone() {

        assertEquals("02 96 48 26 79", placeFormater.getPhoneNumber());

    }

    /**
     * Give api data to place formater should return place id.
     */
    @Test
    public void GiveApiDataToPlaceFormaterShouldReturnPlaceId() {

        assertEquals("La Tête de Goinfre223 route de trégastel, lannion", placeFormater.getId());

    }

    /**
     * Give api data to place formater should return place address.
     */
    @Test
    public void GiveApiDataToPlaceFormaterShouldReturnPlaceAddress() {

        assertEquals("223 Route de Trégastel, Lannion", placeFormater.getPlaceAddress());

    }

    /**
     * Give api data to place formater should return place rate.
     */
    @Test
    public void GiveApiDataToPlaceFormaterShouldReturnPlaceRate() {

        assertEquals(3, placeFormater.getPlaceRate());

    }


    /**
     * Give api data to place formater should return website.
     */
    @Test
    public void GiveApiDataToPlaceFormaterShouldReturnWebsite() {

        assertEquals("http://www.tetedegoinfre.com/", placeFormater.getWebsiteUrl());

    }

}
