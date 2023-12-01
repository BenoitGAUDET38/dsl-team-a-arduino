import os
import subprocess

# Create results directory if not exists
results_dir = "results"
os.makedirs(results_dir, exist_ok=True)

# Iterate through ArduinoML files
for arduinoml_file in os.listdir("scenarios"):
    if arduinoml_file.endswith(".arduinoml"):
        # Get the file name without extension
        file_name_no_ext = os.path.splitext(arduinoml_file)[0]

        # Run Maven command and filter the output using Python
        command = f"mvn exec:java -Dexec.args=scenarios/{arduinoml_file}"
        process = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        output, error = process.communicate()

        # Filter the output content
        start_pattern = "// Wiring code generated from an ArduinoML model"
        end_pattern = "}"
        in_block = False
        content = ""

        for line in reversed(output.split('\n')):
            if end_pattern in line:
                in_block = True
            if in_block:
                content = line + '\n' + content
            if start_pattern in line:
                break

        # Write the filtered content to the result file
        result_file_path = os.path.join(results_dir, f"{file_name_no_ext}.ino")
        with open(result_file_path, "w") as result_file:
            result_file.write(content)

        # Check if the command was successful
        if process.returncode == 0:
            print(f"Processing {arduinoml_file} - Success")
        else:
            print(f"Processing {arduinoml_file} - Failed")
