import dataHelper as dataHelper
import clientMessageHelper as um
from tabulate import tabulate


class InputHelper:

    def __init__(self, cluster):
        self.cluster = cluster

        self.user_options = [
            ['1','transact','node','sender','recipient','amount','Add a transaction to a given node'],
            ['2','add','register (0/1)','<>','<>','<>','Add a node to the network. Will Register with entire network by Default'],
            ['3','register','existing_node','new_node','<>','<>','register a new_node with an existing node'],
            ['4','resolve','node','<>','<>','<>','Tell a Given Node to resolve with its network'],
            ['5','list','<>','<>','<>','<>','SDisplay Info From Each Node in the Cluster'],
            ['6','crash','node_to_contact','<>','<>','<>','Virtually Crash a Given Node in the Cluster'],
            ['7','mine','node_to_contact','<>','<>','<>','Mine a new block'],
            ['8','chain','node','<>','<>','<>','Get All Transactions For a Given node'],
            ['9','resolve','node','<>','<>','<>','Get All Transactions For a Given node']
        ]

    # Print All Commands User Can Enter
    def print_user_options(self):
        print
        print tabulate(self.user_options , headers=['#','Command','Arg 1','Arg 2','Arg 3','Arg 4','Description'])
        print

    # Get Command for a given index
    def get_cmd(option_num):
        return self.user_options[option_num-1][1]

    # Add a new transaction to a node
    def handle_transact(self, tokens):
        if len(tokens) < 4:
            print um.MISSING_ARGS
        else:
            post_body = {
                'sender': tokens[1],
                'recipient' : tokens[2],
                'amount' : tokens[3]
            }
            _ip = self.cluster.nodes[tokens[0]]
            rsp = dataHelper.make_post_request(_ip,'transactions/new',post_body)
            print rsp

    # Add a new node to the cluster
    def handle_add(self,tokens):
        _ip = self.cluster.new_node();
        if len(tokens) == 0 or tokens[0] == '0':
            data = {
                'nodes' : [_ip]
            }
            for _id, url in self.cluster.nodes.iteritems():
                rsp = dataHelper.make_post_request(url,'nodes/register',data);

    # register a node with another node
    def handle_register(self):
        if len(tokens) < 2:
            print um.MISSING_ARGS
        else:
            post_body = {
                'nodes': tokens[1:]
            }
            _ip = self.cluster.nodes[tokens[0]]
            rsp = dataHelper.make_post_request(_ip,'nodes/register',post_body)
            print rsp


    # list data from each node in the cluster
    def handle_list(self,tokens):
        list_header = ['Node','Url','Chain Size']
        data = []
        for _id, url in self.cluster.nodes.iteritems():
            rsp = dataHelper.make_get_request(url,'chain')
            data.append([_id,url,rsp['length']])
        print tabulate(data, headers=list_header)
        print

    # Remove a node from the cluster
    def handle_crash(self,tokens):
        print "Implement this method...."

    # Make a given node mine a block
    def handle_mine(self,tokens):
        if len(tokens) < 1:
            print um.MISSING_ARGS
        else:
            _ip = self.cluster.nodes[tokens[0]]
            rsp = dataHelper.make_request(_ip,"mine/")
            print rsp

    # Show the chain of a given node
    def handle_chain(self,tokens):
        if len(tokens) < 1:
            print um.MISSING_ARGS
        else:
            _ip = self.cluster.nodes[tokens[0]]
            rsp = dataHelper.make_request(_ip,"crash/",tokens)
            print rsp

    # Make a given node resolve with all other nodes
    def handle_resolve(self,tokens):
        if len(tokens) < 1:
            print um.MISSING_ARGS
        else:
            _ip = self.cluster.nodes[tokens[0]]
            rsp = dataHelper.make_request(_ip,"nodes/resolve/")
            print rsp

