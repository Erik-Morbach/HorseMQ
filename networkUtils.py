import random
import struct
import socket

SERVER_HOSTNAME = "localhost"
SERVER_PORT = 80


def createProducerId(producerName):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = bytearray()
    data.append(0b0001000)
    data += producerName.encode('utf-8')
    data += b"\0"
    s.send(data)
    producerId = s.recv(5)
    s.close()
    return producerName, producerId


def createConsumerId(consumerName):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = bytearray()
    data.append(0b0000000)
    data += consumerName.encode('utf-8')
    data += b"\0"
    s.send(data)
    consumerId = s.recv(5)
    s.close()
    return consumerName, consumerId


def createQueueId(allowedConsumers):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = bytearray()
    data.append(0b0010000)
    data += (";".join(allowedConsumers)).encode('utf-8')
    data += b"\0"
    s.send(data)
    queueId = s.recv(5)
    s.close()
    return queueId


def connectConsumerToQueue(consumerId, queueId):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = bytearray()
    data.append(0b0110000)
    data += consumerId.encode("utf-8")
    data += b"\0"
    data += queueId.encode("utf-8")
    data += b"\0"
    s.send(data)
    response = s.recv(1)
    s.close()
    print(response)


def disconnectConsumerToQueue(consumerId, queueId):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = bytearray()
    data.append(0b0100000)
    data += consumerId.encode("utf-8")
    data += b"\0"
    data += queueId.encode("utf-8")
    data += b"\0"
    s.send(data)
    response = s.recv(1)
    s.close()
    print(response)


def producerSendData(producerId, queueId, audioData):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = struct.pack("B", 0b1000000)
    data += struct.pack(f">{len(producerId)}s", producerId.encode("utf-8"))
    data += struct.pack("B", 0)
    data += struct.pack(f">{len(queueId)}s", queueId.encode("utf-8"))
    data += struct.pack("B", 0)
    s.send(data)
    response = s.recv(1)
    if response == 0:
        s.close()
        return

    totalSize = len(audioData)

    print("Sending data as producer ", producerId, " to queue ", queueId)
    index = 0
    while totalSize > 0:
        blockSize = random.randint(1, min(512, totalSize))
        data = struct.pack(">i", blockSize)
        print("Sending data with blockSize=", blockSize)
        for i in range(0, blockSize):
            data += struct.pack(">d", audioData[index])
            index += 1
        s.send(data)
        totalSize -= blockSize


def consumerReceiveData(consumerId, queueId):
    s = socket.socket()
    s.connect((str(SERVER_HOSTNAME), SERVER_PORT))
    data = struct.pack("B", 0b1100000)
    data += struct.pack(f">{len(consumerId)}s", consumerId.encode("utf-8"))
    data += struct.pack("B", 0)
    data += struct.pack(f">{len(queueId)}s", queueId.encode("utf-8"))
    data += struct.pack("B", 0)
    s.send(data)
    response = s.recv(1)
    if response == 0:
        s.close()
        return
    print("Receiving data as a consumer ", consumerId, " at queue ", queueId)

    while 1:
        blockSize = random.randint(1, 20)
        data = struct.pack(">i", blockSize)
        print("Want to receive data with blockSize=", blockSize)
        s.send(data)
        size, = struct.unpack(">i", s.recv(4))
        print(f"will receive {size} of data")
        if size == 0:
            break
        audioData = s.recv(size*8)
        audioData = struct.unpack(f">{size}d", audioData)
        print(f"Received {size} doubles: {",".join([str(a) for a in audioData])}")
    print("End receiving")
