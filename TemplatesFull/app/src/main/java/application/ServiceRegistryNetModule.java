package application;

import android.content.Context;

import dagger.Module;

/**
 * Mostly commented out code in case I want to include networking capabilites
 */
@Module
public class ServiceRegistryNetModule {
    private Context appContext;
    private String baseUrl;

    public ServiceRegistryNetModule(Context appContext) {
        this.appContext = appContext;
    }

    public ServiceRegistryNetModule(Context appContext, String baseUrl) {
        this.appContext = appContext;
        this.baseUrl = baseUrl;
    }

    /*
    OkHttp3 lookup
    Retrofit Lookup
    Retrofit Server Structure:
    Declare OkHttp3
    Declare This and That interceptor to append token into header
    Declare interface of endpoints relative to base URL
    Retrofit.create(interface.class);
     */
    /*
    private final Context context;
    private static final long TIMEOUT_CONST = 0;
    private static final long TIMEOUT_MEDIA_CONST = 30;
    private static final long TIMEOUT_WEATHER_CONSTANT = 10;

    public NetworkProvisions(Context context) {
        this.context = context;

    }
    @Provides
    public Gson provideGsonConverterFactorySettings() {
        //denotes a serializer for dates
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Mountain"));
        JsonSerializer<Date> ser = new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                    context) {
                if (src != null) {
                    //convert date to mountain timezone in all cases
                    Calendar datetime = Calendar.getInstance();
                    datetime.setTime(src);
                    datetime.setTimeZone(TimeZone.getDefault());
                    return new JsonPrimitive(sdf.format(datetime.getTime()));
                } else {
                    return null;
                }
            }
        };

        //denotes a deserializer for dates
        JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json != null) {
                    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    sdf.setTimeZone(TimeZone.getDefault());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(json.getAsLong());
                    calendar.setTimeZone(TimeZone.getTimeZone("Canada/Mountain"));
                    try {
                        return sdf.parse(sdf.format(calendar.getTime()));
                    } catch (ParseException e) {
                        return null;
                    } catch (Exception e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        };
        //creates and returns a gson object with the configured serializer and deserializer
        return new GsonBuilder()
//                .registerTypeAdapter(Date.class, ser)
//                .registerTypeAdapter(Date.class, deser)
                .setLenient()
                .create();
    }
     */
}
