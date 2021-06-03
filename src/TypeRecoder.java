public class TypeRecoder {
   private String[] dealerThings = new String[3];
    private String[] smokerThing = new String[3];
    public TypeRecoder() {
        dealerThings[0] = "matches and tobacco";
        dealerThings[1] = "paper and tobacco";
        dealerThings[2] = "matches and paper";
        smokerThing[0] = "paper";
        smokerThing[1] = "matches";
        smokerThing[2] = "tobacco";

    }




    public String getDealerThings(int i){
        return (dealerThings[i-1]);
    }
    public String getSmokerThings(int i){
        return smokerThing[i-1];
    }


}
