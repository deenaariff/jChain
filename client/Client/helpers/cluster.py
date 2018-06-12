import dataHelper as dh
import time, sys, os
import docker
import shutil

class Cluster:

    def __init__(self, num_nodes, docker_image):

        self.docker_ip = "192.168.99.100"
        self.docker_image = docker_image;

        self.nodes = {};
        self.client = docker.from_env()
        self.containers = []

        try:
            result = self.client.images.get(self.docker_image)
            print "Detected image '" + self.docker_image + "'"
        except docker.errors.ImageNotFound as e:
            print "Error: Image '" + self.docker_image + "' not Found"
            sys.exit(1)

        self.num_nodes = int(num_nodes)
        self.initialize_cluster()
    
    # Start a cluster of docker nodes
    # Delete existing logs 
    def initialize_cluster(self):

        d='./logs/'
        filesToRemove = [os.path.join(d,f) for f in os.listdir( d )]
        for f in filesToRemove:
            os.remove(f) 

        for i in range(1,self.num_nodes+1):

            print "Starting Node: " + str(i)

            result = self.client.containers.run(self.docker_image, detach=True, ports={'5000/tcp': 79 + i})
            self.containers.append(result)

            port = str(79 + i)
            url = "http://" + self.docker_ip + ":" + port

            self.nodes[str(i)] = url;

    # Remove the cluster of docker nodes
    # Add all logs to files in logs/
    def remove_cluster(self):


        print "Stopping " + str(len(self.containers)) + " containers"

        file_index = 1

        for container in self.containers:

            filename = "logs/log" + str(file_index) + ".txt"

            with open(filename, "w+") as f:
                f.writelines(container.logs())
                f.close()

            container.stop()

            file_index += 1