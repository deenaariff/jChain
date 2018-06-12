from helpers.inputHelper import InputHelper as IH
from helpers.cluster import Cluster as Cluster

import helpers.clientMessageHelper as um
import helpers.dataHelper as dh
import sys,time, urllib2

run_client = True  # Run Client in Loop Until User Quits

num_nodes = sys.argv[1];
docker_image = sys.argv[2];

print "Initializing Cluster..."
cluster = Cluster(num_nodes, docker_image)
ih = IH(cluster)

while run_client:

    # Print Commands User Can Enter
    ih.print_user_options()

    # Handle User Response
    text = raw_input(">> ")
    tokens = text.split(" ")

    send = True
    msg = ""
    cmd = tokens[0]
    tokens = tokens[1:]


    try:
        # Create Appropriate MSG Given User cmd
        # Handle Invalid User cmd if necessary
        if cmd == 'q' or cmd == 'exit':
            run_client = False

        elif cmd == ih.get_cmd(0):
            ih.handle_transact(tokens)

        elif cmd == ih.get_cmd(1):
            ih.handle_add(tokens)

        elif cmd == ih.get_register(2):
            ih.handle_register(tokens)

        elif cmd == ih.get_cmd(3):
            ih.handle_resolve(tokens)

        elif cmd == ih.get_cmd(4):
            ih.handle_list()

        elif cmd == ih.get_cmd(5):
            ih.handle_crash(tokens)

        elif cmd == ih.get_cmd(6):
            ih.handle_mine(tokens)

        elif cmd == ih.get_cmd(7):
            ih.handle_chain(tokens)

        elif cmd == ih.get_cmd(8):
            ih.handle_resolve(tokens)

        else:
            print um.INVALID_CMD

    except Exception as e:
        print e.message
        cluster.remove_cluster()
        sys.exit(1)


cluster.remove_cluster()

