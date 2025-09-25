import time
import networkUtils as nu
import struct
import random
import matplotlib
import threading as th
from queue import Queue
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from functools import partial
matplotlib.use('macosx')

consumerName = "Con1"
queueId = "q1"

consumerId = nu.createConsumerId(consumerName)[1].decode("utf-8")

nu.connectConsumerToQueue(consumerId, queueId)


s = nu.consumerBeginReceivingData(consumerId, queueId)

que = Queue()


def updateQueue():
    while 1:
        time.sleep(0.02)
        blockSize = int(0.02*44100)
        data = struct.pack(">i", blockSize)
        s.send(data)
        size, = struct.unpack(">i", s.recv(4))
        if size == 0:
            time.sleep(0.02)
            continue
        data = s.recv(size*8)
        data = struct.unpack(f">{size}d", data)
        que.put(data)
        print(f"{data[0]} queue size: ", que.qsize(), end="            \n")


th.Thread(target=updateQueue).start()


global xdata, ydata, index
fig, ax = plt.subplots()
xdata, ydata = [0], []
ln, = ax.plot([], [])
index = 0
sampleRate = 44100
duration = 2
xdata = np.linspace(0, duration, int(sampleRate*duration))
ydata = [0]*int(sampleRate*duration)


def init():
    ax.set_xlim(0, duration)
    ax.set_ylim(-2, 2)
    return ln,


def update(frame):
    global xdata
    global ydata
    while not que.empty():
        data = que.get()
        for w in data:
            ydata += [w]
        ydata = ydata[len(data):]
    ln.set_data(xdata, ydata)
    return ln,


ani = FuncAnimation(fig, update, cache_frame_data=False,
                    init_func=init, blit=True, interval=0.25)
plt.show()
