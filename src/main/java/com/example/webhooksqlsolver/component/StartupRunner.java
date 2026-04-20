// package com.example.webhooksqlsolver.component;

// import com.example.webhooksqlsolver.service.WebhookService;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// @Component
// public class StartupRunner implements CommandLineRunner {

//     private static final Logger logger =
//             LoggerFactory.getLogger(StartupRunner.class);

//     @Autowired
//     private WebhookService webhookService;

//     @Value("${app.startup.enabled:false}")
//     private boolean startupEnabled;

//     @Override
//     public void run(String... args) {
//         if (startupEnabled) {
//             logger.info("Startup execution enabled - running webhook flow");
//             webhookService.processWebhookFlow();
//         } else {
//             logger.info("Startup execution disabled");
//         }
//     }
// }




package com.example.webhooksqlsolver.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Startup execution disabled");
    }
}