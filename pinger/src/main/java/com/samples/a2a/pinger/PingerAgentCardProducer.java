package com.samples.a2a.pinger;

import io.a2a.server.PublicAgentCard;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/** Producer for Pinger Agent Card. */
@ApplicationScoped
public final class PingerAgentCardProducer {

  /** HTTP port for the agent. */
  @Inject
  @ConfigProperty(name = "quarkus.http.port")
  private int httpPort;

  /**
   * Creates the agent card for the pinger agent.
   *
   * @return the agent card
   */
  @Produces
  @PublicAgentCard
  public AgentCard agentCard() {
    return new AgentCard.Builder()
        .name("Pinger Agent")
        .description(
            "An agent that can send ping and receive pong."
        )
        .url("http://localhost:" + httpPort)
        .version("1.0.0")
        .documentationUrl("http://example.com/docs")
        .capabilities(
            new AgentCapabilities.Builder()
                .streaming(true)
                .pushNotifications(false)
                .stateTransitionHistory(false)
                .build())
        .defaultInputModes(Collections.singletonList("text"))
        .defaultOutputModes(Collections.singletonList("text"))
        .skills(
            List.of(
                new AgentSkill.Builder()
                    .id("pong")
                    .name("Pong")
                    .description(
                        "Receive a pong")
                    .tags(List.of("pong"))
                    .examples(
                        List.of(
                            "(tell,pong)"))
                    .build(),
                    new AgentSkill.Builder()
                            .id("do_ping")
                            .name("Do-Ping")
                            .description(
                                    "Send a ping")
                            .tags(List.of("ping"))
                            .examples(
                                    List.of(
                                            "(achieve,do_ping)"))
                            .build()
            )
        )
        .protocolVersion("0.3.3.Final")
        .build();
  }
}
