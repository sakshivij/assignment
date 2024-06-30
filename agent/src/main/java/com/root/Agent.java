package com.root;
import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.http.HttpRequest;
import java.security.ProtectionDomain;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

class Agent {

  public static void premain(String agentArgs, Instrumentation inst) {


        String[] args = agentArgs.split(",");
        String envVarValue = null;
        for (String arg : args) {
            if (arg.startsWith("HT_MODE=")) {
                envVarValue = arg.substring("HT_MODE=".length());
                break;
            }
        }
        // Use the environment variable value as needed
        System.out.println("Mode: " + envVarValue);
    
        // if(envVarValue.equals("Record")){

        //     new AgentBuilder.Default()
        //     .disableClassFormatChanges()
        //     .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
        //     .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
        //     .ignore(none())
        //     .type(ElementMatchers.nameStartsWith("com.root.service"))
        //     .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
        //         builder = builder.visit(Advice
        //                 .to(ApiRecordAdvice.class)
        //                 .on(ElementMatchers.named("makeApiCall"))
        //         );
        //         builder = builder.visit(Advice
        //                 .to(DatabaseRecordAdvice.class)
        //                 .on(ElementMatchers.named("saveBlogPost"))
        //         );
        //         return builder;
        //     })
        //     .installOn(inst);            
        // }
        // else{
        //     new AgentBuilder.Default()
        //     .disableClassFormatChanges()
        //     .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
        //     .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
        //     .ignore(none())
        //     .type(ElementMatchers.nameStartsWith("com.root.service"))
        //     .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
        //         builder = builder.visit(Advice
        //                 .to(ApiReplayAdvice.class)
        //                 .on(ElementMatchers.named("makeApiCall"))
        //         );
        //         builder = builder.visit(Advice
        //                 .to(DatabaseReplayAdvice.class)
        //                 .on(ElementMatchers.named("saveBlogPost"))
        //         );
        //         return builder;
        //     })
        //     .installOn(inst);            

        // }

        // new AgentBuilder.Default()
        //         .type(ElementMatchers.nameContains("com.root.service")) // Replace with your class name
        //         .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
        //                 builder.method(ElementMatchers.nameContains("getRequestBuilder")) // Replace with your method name
        //                         .intercept(MethodDelegation.to(CallInterceptor.class))
        //         ).installOn(inst);

        new AgentBuilder.Default()
                .type(ElementMatchers.nameContains("com.root.java.net.http")) // Target java.net.http.HttpRequest
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                        return builder.method(ElementMatchers.named("newBuilder")) // Find the method newBuilder()
                        .intercept(MethodDelegation.to(CallInterceptor.class));
                    }
                })
                .installOn(inst);

    }

    public static class CustomURIInterceptor {
        private final String newUri;

        public CustomURIInterceptor(String newUri) {
            this.newUri = newUri;
        }

        public URI intercept() {
            return URI.create(newUri);
        }
    }
    
    public static class CallInterceptor {
        public static HttpRequest.Builder getRequestBuilder() {
            return HttpRequest.newBuilder(URI.create("https://google.com"));
                    // .uri(URI.create("https://google.com")) // New URI here
                    // .GET()
                    // .build();
        }
    }
}