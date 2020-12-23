package org.transactions.configuration;


//@Configuration
public class MongoConfiguration {

//    @Autowired
//    private Environment env;
//
//    @Value("${spring.data.mongodb.uri}")
//    private String mongoDbUri;
//
//    @Value(("${spring.data.mongodb.database}"))
//    private String databaseName;
//
//
//    public @Bean
//    MongoClient mongoClient() {
//        return MongoClients.create(mongoDbUri);
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() {
//
//        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
//                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .codecRegistry(pojoCodecRegistry)
//                .build();
//        MongoClient mongoClient = MongoClients.create(settings);
//
//        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "transactions-staging");
//
//        return mongoTemplate;
//
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return "transactions-staging";
//    }
//
//    @Override
//    protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter adapter) {
//        adapter.registerConverter(new DateToOffsetDateTimeConverter());
//        adapter.registerConverter(new OffsetDateTimeToDateConverter());
//    }
}
