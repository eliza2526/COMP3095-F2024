package ca.gbc.apigateway.routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;


@Configuration
@Slf4j
public class Routes {

    @Value("http://localhost:8082")
    //@Value("${services.product-url}")
    private String productServiceUrl;

    @Value("http://localhost:8085")
    //@Value("${services.order-url}")
    private String orderServiceUrl;

    @Value("http://localhost:8083")
    //@Value("${services.inventory-url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){

        //log.info("Creating routes");

        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product"), request -> {

                   // log.info("Received request for product service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(productServiceUrl).handle(request);
                        //log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e){
                      //  log.error("Error occured while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to product-service");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute(){

       // log.info("Initializing product service route with URL: {}", orderServiceUrl);

        return GatewayRouterFunctions.route("order-service")
                .route(RequestPredicates.path("/api/order"), request -> {

                   // log.info("Received request for order service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
                     //   log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e){
                    //    log.error("Error occured while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to order-service");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute(){

        //log.info("Creating routes");

        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), request -> {

                    // log.info("Received request for product service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(inventoryServiceUrl).handle(request);
                        //log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e){
                        //  log.error("Error occured while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to inventory-service");
                    }
                })
                .build();
    }


    public RouterFunction<ServerResponse> productServiceSwaggerRoute(){

        return GatewayRouterFunctions.route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product_service/v3/api_docs"),
                        HandlerFunctions.http(productServiceUrl))
                .filter(setPath("/api-docs"))
                .build();
    }

    public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){

        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order_service/v3/api_docs"),
                        HandlerFunctions.http(orderServiceUrl))
                .filter(setPath("/api-docs"))
                .build();
    }

    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){

        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory_service/v3/api_docs"),
                        HandlerFunctions.http(inventoryServiceUrl))
                .filter(setPath("/api-docs"))
                .build();
   }
}