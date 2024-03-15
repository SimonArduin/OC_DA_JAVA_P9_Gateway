# OC_DA_JAVA_P9_Gateway

Gateway microservice for [medilabo](https://github.com/SimonArduin/OC_DA_JAVA_P9_Medilabo)

This application receives http requests and reroutes them to the appropriate microservice, if the requests are authorized.

It is built with Spring as a Gradle project, using the following modules :
- Spring Security
- Spring Cloud Gateway

Incoming requests are received on port 8010 (specified in [application.yml](gateway/src/main/resources/application.yml)), then authorized according to URL, role, and request type :
<table>
  <thead>
    <tr>
      <th></th>
      <th>/patient/</th>
      <th>/prediction/</th>
      <th>/note/</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>PLANNER</td>
      <td>All</td>
      <td>None</td>
      <td>Get</td>
    </tr>
    <tr>
      <td>PRACTITIONER</td>
      <td>Get</td>
      <td>Get</td>
      <td>All</td>
    </tr>
    <tr>
      <td>PREDICTIONMICROSERVICE</td>
      <td>Get</td>
      <td>Get</td>
      <td>None</td>
    </tr>
  </tbody>
</table>

The routes are also defined in application.yml.
