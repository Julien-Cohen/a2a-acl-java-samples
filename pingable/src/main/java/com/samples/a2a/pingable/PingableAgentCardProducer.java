package com.samples.a2a.pingable;

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

/** Producer for Content Writer Agent Card. */
@ApplicationScoped
public final class PingableAgentCardProducer {

  /** HTTP port for the agent. */
  @Inject
  @ConfigProperty(name = "quarkus.http.port")
  private int httpPort;

  /**
   * Creates the agent card for the content writer agent.
   *
   * @return the agent card
   */
  @Produces
  @PublicAgentCard
  public AgentCard agentCard() {
    return new AgentCard.Builder()
        .name("Pingable Agent")
        .description(
            "An agent that can receive ping and replies with pong."
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
            Collections.singletonList(
                new AgentSkill.Builder()
                    .id("ping")
                    .name("Ping")
                    .description(
                        "Receive a ping and would reply with a pong")
                    .tags(List.of("ping"))
                    .examples(
                        List.of(
                            "ping"))
                    .build()))
        .protocolVersion("0.3.3.Final")
        .build();
  }
}
