import socket

SERVER_HOSTNAME = "localhost"
SERVER_PORT = 80


def createProducerId(producerName):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = bytearray()
    data.append(0b00010000)
    data += producerName.encode('utf-8')
    data += b"\0"
    s.send(data)
    producerId = s.recv(4)
    s.close()
    return producerName, producerId


def createQueueId(allowedConsumers):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = bytearray()
    data.append(0b00100000)
    data += (";".join(allowedConsumers)).encode('utf-8')
    data += b"\0"
    s.send(data)
    queueId = s.recv(4)
    s.close()
    return queueId
