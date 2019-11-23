    String json = loadJSONFromAsset();
        
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray insurancesArray = jsonObject.getJSONArray("insurances");

            for (int i = 0; i<insurancesArray.length(); i++)
            {
                JSONObject insuranceObject = insurancesArray.getJSONObject(i);

                Insurance insurance = new Insurance();

                insurance.setIdx(insuranceObject.getInt("idx"));
                insurance.setProductName((insuranceObject.getString("productName")));
                insurance.setCompany(insuranceObject.getString("company"));
                insurance.setProductType(insuranceObject.getString("productType"));
                insurance.setMinAge(insuranceObject.getInt("minAge"));
                insurance.setMaxAge(insuranceObject.getInt("maxAge"));
                insurance.setPrice(insuranceObject.getInt("price"));
                insurance.setScore(insuranceObject.getDouble("score"));

                insuranceList.add(insurance);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }


    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("insurance.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }