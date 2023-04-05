# Carpooling application

Final Project Assignment - Telerik Academy Alpha - Java

## Project Description
The application connects people looking to travel with drivers heading the same way, so they can travel together and share the cost.

Carpooling also creates a unique space, enabling exchanges between people who might have never met otherwise but who come together to share a ride.

--------------------

**Public Part**


The public part of the project includes the available trips created by other users with an option to
sort and filter them, the **user login** and **user registration forms**.

--------------------

**Private Part**

**Registered user can:**
- Change his/her information and upload an avatar image
- Create a trip as driver
- Apply for a trip as a passenger. Users that apply for the trip should be approved by the driver (owner of the trip)


The user who creates travel sets the starting and ending points, the departure
time and the number of free seats in his/her car which are mandatory for a trip post.
The driver also can place optional message or restrictions like “no pets”, “no smoking” and/or “without luggage”.
The driver can approve/decline passengers from the candidates pool.
If a passenger apply for a trip and it is approved by the driver he still may cancel
his participation. The driver can also reject a passenger.
The driver can cancel the trip before the time of departure. He also should mark
his trip as ongoing or complete.
When the trip complete the passengers can leave feedback about the driver as
well as the driver can leave a feedback for every passenger. The driver can also leave a negative feedback if a passenger does not arrive at the set time and place for the starting point and vice versa.

Every user have average rating as a driver and as a passenger.

**Trello Board:** [OPEN](https://trello.com/b/VKn3ezrx/carpooling-application)

##To start the application

###Local installation guidelines:

1. Install docker

2. Clone the repository with http or ssh

3. Build the project by using Gradle

       ./gradlew build

5. Create a docker container by the Dockerfile in the project

       docker build -t carpooling:<TAG> .


###AzureDevOps guidelines

1. Create a docker hub account

2. Add docker hub registry credentials in AzureDevOps

``Project settings > Service connections > New service connection ``

/Note that the Service connection name should be 'Docker reg'.

Otherwise, you need to modify the pipeline - tag dockerRegistryEndpoint: {service connection name}

3. Run the pipeline. The pipeline will build and push a docker image in your docker hub repository



### Run the project in k8s cluster
####Install minikube
https://minikube.sigs.k8s.io/docs/start/

####Load docker image in minikube

    minikube image load <image-name>

####Create a new namespace
    kubectl create namespace carpooling


####Setting the namespace preference. You can permanently save the namespace for all subsequent kubectl commands in that context.
	kubectl config set-context --current --namespace=<insert-namespace-name-here>

####Apply k8s manifest files
`kubectl apply -f carpooling-ingress.yaml`

`kubectl apply -f mysql-config.yaml`

`kubectl apply -f mysql-secrets.yaml`

`kubectl apply -f mysql-deployment.yaml`

`kubectl apply -f carpooling-deployment.yaml`
 