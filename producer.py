import networkUtils as nu
import struct
import numpy as np
import random
import time
from queue import Queue
import threading as th
from sys import argv


amp = 1
freq = 2  # hz
duration = 10  # s
sampleRate = 44100
waveType = argv[1]

producerName = argv[2]
queueName = argv[3]
consumerList = argv[4:]

if nu.createProducerId(producerName):
    print(f"Producer {producerName} created")

if nu.createQueueId(producerName, queueName,  consumerList):
    print(f"Queue {queueName} created for consumers {consumerList}")

print(f"producer = {producerName}")
print(f"queue = {queueName}")

s = nu.producerBeginSendData(producerName, queueName)


inputQueue = Queue()


def populateQueue():
    t0 = time.perf_counter()
    index = 0
    while 1:
        time.sleep(0.2)
        t1 = time.perf_counter()
        diff = t1 - t0
        tt0 = time.perf_counter()
        if waveType == "0":
            inputQueue.put(
                np.sin(2*np.pi*freq*np.linspace(t0, t1, int(diff*sampleRate))))
        else:
            data = []
            for i in range(0, int(diff*sampleRate)):
                data += [index/sampleRate]
                index += 1
                if index == sampleRate:
                    index = 0
            inputQueue.put(data)
        t0 = tt0


th.Thread(target=populateQueue).start()

data = []
while 1:
    if len(data) == 0:
        data = inputQueue.get()
    currentBlock = random.randint(1, min(512, len(data)))
    sendData = struct.pack(">i", currentBlock)
    # print(f"Sending {currentBlock} values")
    sendData += struct.pack(">"+str(currentBlock)+"d", *data[:currentBlock])
    # print(f"Sended {len(sendData)}")
    s.send(sendData)
    data = data[currentBlock:]
