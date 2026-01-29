package org.achymake.essentials.files;

import java.util.ArrayList;
import java.util.HashMap;

public record EssentialsConfig(String SpawnWorld, ArrayList<String> Censored, String Format, String Currency, boolean CurrencyPrefix, HashMap<String, Integer> MaxHomes) {
}