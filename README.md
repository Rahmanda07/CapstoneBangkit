Sure! Here is a comprehensive README description for your GitHub repository:

---

# Freshwater Fish Disease Classification

This repository contains a deep learning project aimed at classifying freshwater fish diseases using a Convolutional Neural Network (CNN) model. The dataset used for this project is sourced from the "Freshwater Fish Disease Aquaculture in South Asia" dataset available on Kaggle. This project involves data preprocessing, model training, evaluation, and saving the trained model.

## Dataset

The dataset includes images of fish categorized into the following classes:

1. Bacterial Red disease
2. Bacterial diseases - Aeromoniasis
3. Bacterial gill disease
4. Fungal diseases Saprolegniasis
5. Healthy Fish
6. Parasitic diseases
7. Viral diseases White tail disease

### Data Preparation

- The dataset is downloaded from Kaggle and extracted into the specified directory structure.
- The images are split into training and validation sets with an 80-20 ratio.
- Data augmentation and preprocessing are performed using `ImageDataGenerator`.

## Model Architecture

Two models were experimented with in this project:

1. **Transfer Learning with EfficientNetB1**:
    - The EfficientNetB1 model pretrained on ImageNet is used as the base model.
    - Additional layers including batch normalization, dense layers, and dropout are added.
    - The output layer consists of a dense layer with 7 units (one for each class) and softmax activation.

2. **Custom CNN Model**:
    - A simple Sequential model is created with multiple Conv2D and MaxPooling2D layers.
    - Flattening followed by dense layers is applied for classification.
    - The output layer is similar to the transfer learning model, with softmax activation.

### Custom CNN Model Layers:

```plaintext
Layer (type)                     Output Shape          Param #
=================================================================
conv2d (Conv2D)                  (None, 148, 148, 32)  896
max_pooling2d (MaxPooling2D)     (None, 74, 74, 32)   0
conv2d_1 (Conv2D)                (None, 72, 72, 64)   18496
max_pooling2d_1 (MaxPooling2D)   (None, 36, 36, 64)   0
flatten (Flatten)                (None, 82944)        0
dense (Dense)                    (None, 128)          10616960
dense_1 (Dense)                  (None, 7)            903
=================================================================
Total params: 10,635,255
Trainable params: 10,635,255
Non-trainable params: 0
```

### Model Compilation and Training

- The model is compiled with `categorical_crossentropy` loss and `adam` optimizer.
- Training is done for 10 epochs, with steps per epoch set to the length of the training data generator.
- Early stopping and learning rate reduction callbacks are used to prevent overfitting.

### Evaluation and Results

- The model is evaluated on the validation dataset.
- Accuracy and loss metrics are plotted for both training and validation sets.

### Model Saving

- The trained model's architecture is saved in JSON format.
- The model weights are saved as binary files for each layer.
- All model files are compressed into a ZIP file for easy download and sharing.

## Usage

### Prerequisites

Ensure you have the necessary libraries installed:

```bash
pip install tensorflow kaggle matplotlib
```

### Downloading and Extracting the Dataset

Make sure to download the dataset from Kaggle and place the `kaggle.json` API key file in the correct location.

```bash
kaggle datasets download -d subirbiswas19/freshwater-fish-disease-aquaculture-in-south-asia
unzip freshwater-fish-disease-aquaculture-in-south-asia.zip -d /content/data/
```

### Running the Training Script

Execute the script to preprocess data, train the model, and save the results:

```bash
python train_model.py
```

### Accessing the Model

The trained model and weights can be found in the `model_files.zip`. Extract the contents to use the model for inference or further training.

## Conclusion

This project demonstrates a robust approach to classifying freshwater fish diseases using deep learning. The combination of transfer learning and custom CNN models provides a comprehensive solution for the task. Feel free to contribute or suggest improvements by opening an issue or pull request.
