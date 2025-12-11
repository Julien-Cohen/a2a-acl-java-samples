# a2a-acl-java-samples

Examples of use of a2a-acl-java library.

To run an agent, first install locally the library with maven, then, in the directory of the agent project, run the following:
```bash
  mvn quarkus:dev
```

Alternative :
```bash
    cd PATH/TO/pingable
    mvn clean
    mvn package
    mvn quarkus:run
```


## Test Pingable alone

Run the Pingable agent (see above), then, run the client Python script from `a2a-acl` :
```bash
  cd PATH/TO/a2a-acl/tests/ping_java_acl_agent
  python3 run_test_client.py
```

## Test Pingable with Pinger

Run the Pingable agent (see above), then, run the Pinger agent (see above), then run client Python script from `a2a-acl` :
```bash
  cd PATH/TO/a2a-acl/tests/clients/do_ping_client
  python3 run_test_client.py
```