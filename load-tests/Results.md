# Load Test Results

# Setup
Since I was running these simulations on my local machine, per Gatling instructions, I increased the open file limit from 2560 to 65536.
Run `ulimit -n` to increase the limit in the current shell. This allows many new sockets to be opened and achieve heavy load.

# Scenarios
Scenario 1: Baseline
One RPC method with static params on the i3 node

Scenario 2: Compare two methods
How our different RPC methods stand against each other
Scenario 2a. Load test - simulated expectant # of users
Scenario 2b. Stress test - 1 ms high number of users

Scenario 3: Soak test
Now we’ll run the locust benchmarks all together in one test suit. We instruct locust to call each RPC method with equal probability, and to let each worker hit the method as quickly as possible (for example, every 1 ms).

# Hypothesis
Given test scenario and computational limitations by running load tests on my local machine (Macbook Pro - 3.1 GHz Dual-Core Intel Core i5),
the hypothesis is x

Upper bounds for test:
- 1 machine
- Potential to being rate limited

# Results

Test Scenario