const downloadPdfFromFile = async (file) => {
  if (!file || !(file instanceof File)) {
    console.error("Invalid file provided.");
    return;
  }

  const fileURL = URL.createObjectURL(file);

  const link = document.createElement("a");
  link.href = fileURL;
  link.download = file.name;

  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);

  URL.revokeObjectURL(fileURL);
};

export default downloadPdfFromFile;
