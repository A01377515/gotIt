package mx.itesm.wkt.gotita;

import android.support.annotation.NonNull;

public class OfferId {
    public String offerId;

    public <T extends OfferId> T withId(@NonNull final String id){
        this.offerId = id;
        return (T) this;
    }
}
