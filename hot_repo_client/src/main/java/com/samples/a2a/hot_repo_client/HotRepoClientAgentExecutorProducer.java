package com.samples.a2a.hot_repo_client;

import io.a2a.A2A;
import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.events.EventQueue;
import io.a2a.spec.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import mosaico.acl.ACLMessage;
import mosaico.acl.BDIAgentExecutor;
import org.eclipse.microprofile.config.inject.ConfigProperty;


/**
 * Hot-Repository Client Agent Executor.
 */
@ApplicationScoped
public final class HotRepoClientAgentExecutorProducer {

    /** HTTP port for the agent. */
    @Inject
    @ConfigProperty(name = "quarkus.http.port") // defined in file resources/application.properties
    private int httpPort;

    /**
     * Creates the agent executor for the client agent.
     *
     * @return the agent executor
     */
    @Produces
    public AgentExecutor agentExecutor() {
        return new ClientAgentExecutor();
    }

    /**
     * Agent executor implementation for hot-repo client agent service.
     */
    private class ClientAgentExecutor extends BDIAgentExecutor {

        final String myUrl = "http://127.0.0.1:" + httpPort;
        static final String hotRepoUrl = "http://127.0.0.1:9970";

        static final String skillDescr = "mandatory_skill(achieve, do_jump,0)";

        @Override
        public void executeTell(final ACLMessage m,
                            final EventQueue eventQueue) throws JSONRPCError {
            System.out.println("Received a tell message.");
            if (m.content().startsWith("selected")) {
                System.out.println("Received a tell/selected message: " + m.content());
                System.out.println("Decoded selection: " + decodeSelection(m.content()));
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : tell/selected received."));
            }
            else {
                System.out.println("Unknown request (only receive selected with tell)." );
                eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
            }
        }

        @Override
        public void executeAchieve(final ACLMessage m,
                                final EventQueue eventQueue) throws JSONRPCError {
            System.out.println("Received an achieve message.");
            if (m.content().startsWith("go")) {
                System.out.println("Received a achieve/go request");
                eventQueue.enqueueEvent(A2A.toAgentMessage("Ack : achieve/go received."));

                spawn_send_message(hotRepoUrl, myUrl, "ask", "python_agentspeak_codec", "by_skills(["+skillDescr + "|[]])");
            }
            else {
                System.out.println("Unknown request (only receive go with achieve)." );
                eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
            }
        }

        @Override
        public void executeAsk(final ACLMessage m,
                                final EventQueue eventQueue) throws JSONRPCError {
            System.out.println("Received an ask message.");
            System.out.println("Unknown request (cannot receive ask messages)." );
            eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
        }

        @Override
        public void executeOther(final ACLMessage m,
                                final EventQueue eventQueue) throws JSONRPCError {
            System.out.println("Received a message with unsupported illocution: " + m.illocution());
            eventQueue.enqueueEvent(A2A.toAgentMessage("KO : Unknown request."));
        }

    /** Returns a new string without selected(" at the beginning and ") at the end. */
    static String decodeSelection(String s){
            assert (s.startsWith("selected(") && s.endsWith(")"));
            final String s2 = s.substring("selected(-".length());
            final String s3 = s2.substring(0, s2.length()-2);
            return s3;

    }


    }
}
