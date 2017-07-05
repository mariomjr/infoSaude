package br.ufg.inf.infosaude.services;

import br.ufg.inf.infosaude.client.RetrofitClient;

/**
 * Created by astr1x on 01/07/17.
 */

public class ServicesUtils {

    public static final String URL_BASE = "https://private-ddf14-infosaude.apiary-mock.com/";

    private ServicesUtils() {
    }

    public static UserService getUserService() {
        return RetrofitClient.getClient(URL_BASE).create(UserService.class);
    }

    public static HospitalService getHospitalService() {
        return RetrofitClient.getClient(URL_BASE).create(HospitalService.class);
    }
}
