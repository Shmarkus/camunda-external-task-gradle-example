package ee.codehouse.camunda;

import java.util.logging.Logger;

import org.camunda.bpm.client.ExternalTaskClient;

public class ChargeCardWorker {
    private static final Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());
    private static final String BASE_URL = "http://192.168.99.100:8080/engine-rest";
    private static final String TOPIC_NAME = "charge-card";

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl(BASE_URL)
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe(TOPIC_NAME)
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    // Get a process variable
                    String item = externalTask.getVariable("item");
                    Long amount = externalTask.getVariable("amount");
                    LOGGER.info("Charging credit card with an amount of '" + amount + "'€ for the item '" + item + "'...");

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
